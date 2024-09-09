"use client";

import { useForm } from "react-hook-form";

import Link from "next/link";
import { useRouter } from "next/navigation";

import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardContent, CardTitle, CardFooter } from "@/components/ui/card";
import { Form } from "@/components/ui/form";

import EmailFormControl from "@/components/EmailFormControl";
import PasswordFormControl from "@/components/PasswordFormControl";

const formSchema = z.object({
  email: z.string().email(),
  password: z.string().min(6).max(50),
});

type FormValues = z.infer<typeof formSchema>;

const Page: React.FC = () => {
  const router = useRouter();

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const { isSubmitting } = form.formState;

  const onSubmit = async (values: FormValues) => {
    const response = await fetch("/api/login", {
      method: "POST",
      body: JSON.stringify(values),
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json"
      },
    });

    if (response.ok) {
      router.push("/budget-select");
    } else {
      const message = response.status >= 500 ? "Server error" : "Invalid email or password";
      form.setError("email", { message });
      form.setError("password", { message });
    }
  }

  return (
    <main className="min-w-dvw min-h-dvh flex justify-center items-center">
      <Card>
        <CardHeader>
          <CardTitle>Login</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-4 w-full items-center">
              <EmailFormControl control={form.control} name="email" />
              <PasswordFormControl control={form.control} name="password" dispName="Password" />
              <Button type="submit" className="w-1/3" disabled={isSubmitting}>Login</Button>
            </form>
          </Form>
        </CardContent>
        <CardFooter>
          <div className="flex flex-col w-full items-end">
            <Link href="/signup">Sign up</Link>
            <Link href="/forgot-password"> Forgot password?</Link>
          </div>
        </CardFooter>
      </Card>
    </main>
  )
}

export default Page;
