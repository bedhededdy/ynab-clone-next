"use client";

import { useForm } from "react-hook-form";

import Link from "next/link";
import { useRouter } from "next/navigation";

import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button } from "@/app/_components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/app/_components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/app/_components/ui/form";
import { Input } from "@/app/_components/ui/input";

const formSchema = z.object({
  budgetName: z.string().min(1).max(100)
});

type FormValues = z.infer<typeof formSchema>;

const Page: React.FC = () => {
  const router = useRouter();

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      budgetName: "",
    },
  });

  const onCancelClicked = () => {
    router.push("/budget-select");
  }

  const { isSubmitting } = form.formState;

  const onSubmit = async (values: FormValues) => {
    // FIXME: HAVE TO MANUALLY INCLUDE CONTENT TYPE HERE
    //        WHEN THE CONFIG SHOULD BE HANDLING IT ALREADY
    //        COOKIE IS STILL BEING SENT THOUGH WITHOUT CREDENTIALS INCLUDE
    const response = await fetch("/api/createBudget", {
      method: "POST",
      body: JSON.stringify(values),
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
      },
    });

    if (response.ok) {
      router.push("/budget-select");
      router.refresh();
    } else {
      let message = "";
      if (response.status >= 500) {
        message = "Server error";
      } else if (response.status === 403) {
        router.push("/login");
      } else {
        message = "Budget with this name already exists";
      }
      form.setError("budgetName", { message });
    }
  }

  return (
    <main className="min-w-dvw min-h-dvh flex justify-center items-center">
      <Card>
        <CardHeader>
          <CardTitle>Create Budget</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-4 w-full items-center">
              <div>
                <FormField
                  name="budgetName"
                  control={form.control}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Budget Name</FormLabel>
                      <FormControl>
                        <Input size={50} {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
              <div className="flex w-full justify-end gap-2" >
                <Button type="submit" disabled={isSubmitting}>Create</Button>
                <Button type="button" variant="secondary" onClick={onCancelClicked}>Cancel</Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </main>
  );
}

export default Page;

