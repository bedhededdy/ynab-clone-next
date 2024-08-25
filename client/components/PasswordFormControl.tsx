import { Control, FieldValues, Path } from "react-hook-form";
import { FormItem, FormLabel, FormControl, FormMessage, FormField } from "@/components/ui/form";
import { Input } from "@/components/ui/input";

interface IPasswordFormControlProps<T extends FieldValues> {
  control: Control<T>;
  name: Path<T>;
  dispName: string;
}

const PasswordFormControl = <T extends FieldValues>({ control, name, dispName }: IPasswordFormControlProps<T>) => (
  <FormField
    name={name}
    control={control}
    render={({ field }) => (
      <FormItem>
        <FormLabel>{dispName}</FormLabel>
        <FormControl>
          <Input type="password" size={33} className="text-center" {...field} />
        </FormControl>
        <FormMessage />
      </FormItem>
    )}
  />
);

export default PasswordFormControl;
